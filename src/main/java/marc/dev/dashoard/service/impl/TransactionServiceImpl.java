package marc.dev.dashoard.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import marc.dev.dashoard.entity.TransactionEntity;
import marc.dev.dashoard.repository.TransactionRepository;
import marc.dev.dashoard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Page<TransactionEntity> getTransactions(int page, int size, String sort, String search) {
        // Parse the sort parameter
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortOrder = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sortOrder);

        if (search != null && !search.isEmpty()) {
            return transactionRepository.findBySearchTerm(search, pageable);
        } else {
            return transactionRepository.findAll(pageable);
        }
    }
}

