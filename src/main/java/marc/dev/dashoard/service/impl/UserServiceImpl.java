package marc.dev.dashoard.service.impl;

import lombok.extern.slf4j.Slf4j;
import marc.dev.dashoard.cache.CacheStore;
import marc.dev.dashoard.dto.User;
import marc.dev.dashoard.dto.UserPerformanceDto;
import marc.dev.dashoard.entity.ConfirmationEntity;
import marc.dev.dashoard.entity.CredentialEntity;
import marc.dev.dashoard.entity.TransactionEntity;
import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.enumeration.LoginType;
import marc.dev.dashoard.enumeration.Role;
import marc.dev.dashoard.event.UserEvent;
import marc.dev.dashoard.exception.ApiException;
import marc.dev.dashoard.repository.ConfirmationRepository;
import marc.dev.dashoard.repository.CredentialRepository;
import marc.dev.dashoard.repository.TransactionRepository;
import marc.dev.dashoard.repository.UserRepository;
import marc.dev.dashoard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static marc.dev.dashoard.enumeration.EventType.REGISTRATION;
import static marc.dev.dashoard.enumeration.EventType.RESETPASSWORD;
import static marc.dev.dashoard.utils.UserUtils.createUserEntity;
import static marc.dev.dashoard.utils.UserUtils.fromUserEntity;
import static marc.dev.dashoard.validation.UserValidation.verifyAccountStatus;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
     private BCryptPasswordEncoder encoder;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    private CacheStore<String, Integer> userCache;

    @Override
    public void createUser(String name, String email, String password) {

       var existingUser = userRepository.findByEmailIgnoreCase(email);
       if(existingUser.isPresent()){
           throw new ApiException("Email already exists. Use a different email and try again");
       }

       var userEntity = userRepository.save(createNewUser(name,email.toLowerCase()));
       var credentialEntity = new CredentialEntity(userEntity, encoder.encode(password));
       credentialRepository.save(credentialEntity);
       var confirmationEntity = new ConfirmationEntity(userEntity);
       confirmationRepository.save(confirmationEntity);
       publisher.publishEvent(new UserEvent(userEntity, REGISTRATION, Map.of("key", confirmationEntity.getToken())));

    }
    @Override
    public User getUserById(String userId) {
        var userEntity = userRepository.findUserById(userId).orElseThrow(() -> new ApiException("User not found"));
        return fromUserEntity(userEntity,  getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(String userId) {

        var credentialById = credentialRepository.getCredentialByUserEntityId(userId);
        return credentialById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);

        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if (userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public void resetPassword(String email) {
        System.out.println(email);
        var user = getUserEntityByEmail(email);
        var confirmation = getUserConfirmation(user);


        if(confirmation != null) {
            publisher.publishEvent(new UserEvent(user, RESETPASSWORD, Map.of("key", confirmation.getToken())));
        }else {
            var confirmationEntity = new ConfirmationEntity(user);
            confirmationRepository.save(confirmationEntity);
            publisher.publishEvent(new UserEvent(user, RESETPASSWORD, Map.of("key", confirmationEntity.getToken())));
        }

    }
    @Override
    public User verifyPasswordKey(String key) {
        var confirmationEntity = getUserConfirmation(key);
        if(confirmationEntity == null) {
            throw new ApiException("Unable to find token");
        }
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        if(userEntity == null) {
            throw new ApiException("Incorrect Token");
        }
        verifyAccountStatus(userEntity);
        confirmationRepository.delete(confirmationEntity);
        return fromUserEntity(userEntity, getUserCredentialById(userEntity.getId()));
    }

    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var user = getUserByUserId(userId);
        var credential = getUserCredentialById(user.getId());
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }


    @Override
    public void verifyAccount(String key) {
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }
    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserById(userId).orElseThrow(() -> new ApiException("User not found"));
        return fromUserEntity(userEntity, getUserCredentialById(userEntity.getId()));
    }


    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);

        return fromUserEntity(userEntity,  getUserCredentialById(userEntity.getId()));
    }
    @Override
    public List<UserEntity> getUsersAdmin() {
        List<UserEntity> users = userRepository.findAll();
        return   users.stream()
                .filter(userEntity -> userEntity.getRole().equals("admin"))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> getUsersGeography() {
        List<UserEntity> users = userRepository.findAll();

        Map<String, Long> mappedLocations = users.stream()
                .collect(Collectors.groupingBy(
                        user -> getCountryIso3(user.getCountry()),
                        Collectors.counting()
                ));

        return mappedLocations.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", entry.getKey());
                    map.put("value", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    private String getCountryIso3(String countryName) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getCountry().equalsIgnoreCase(countryName)) {
                return locale.getISO3Country();
            }
        }
        return countryName;
    }
    @Override
    public Page<UserEntity> getUsers(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findUsersByName(name, pageable);
    }

    @Override
    public User getUser(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));

        return fromUserEntity(userEntity);
    }

    @Override
    public UserPerformanceDto getUserPerformance(String userId) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(userId)),
                Aggregation.lookup("affiliatestats", "_id", "userId", "affiliateStats")
        );

        AggregationResults<UserEntity> results = mongoTemplate.aggregate(aggregation, "users", UserEntity.class);
        UserEntity userWithStats = results.getUniqueMappedResult();

        // Log the aggregated results
        System.out.println("Aggregated user with stats: " + userWithStats);

        if (userWithStats == null || userWithStats.getAffiliateStats().isEmpty()) {
            log.info("User or affiliate stats not found");
            return null;
        }

        List<String> affiliateSalesIds = userWithStats.getAffiliateStats().stream()
                .flatMap(affiliateStat -> affiliateStat.getAffiliateSales().stream())
                .collect(Collectors.toList());

        // Log the affiliate sales IDs
        System.out.println("Affiliate sales IDs: " + affiliateSalesIds);

        List<TransactionEntity> saleTransactions = affiliateSalesIds.stream()
                .map(transactionId -> mongoTemplate.findById(transactionId, TransactionEntity.class))
                .filter(transaction -> transaction != null)
                .collect(Collectors.toList());

        // Log the sale transactions
        System.out.println("Sale transactions: " + saleTransactions);

        return new UserPerformanceDto(userWithStats, saleTransactions);
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByToken(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }
    private ConfirmationEntity getUserConfirmation(UserEntity user) {
        return confirmationRepository.findByUserEntity(user).orElse(null);
    }
    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private UserEntity createNewUser(String name, String email) {
        var role = Role.USER.getValue();
        return createUserEntity(name, email, role);
    }
}
