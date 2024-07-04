package marc.dev.dashoard.service;

import marc.dev.dashoard.entity.TransactionEntity;
import org.springframework.data.domain.Page;

public interface TransactionService {

    Page<TransactionEntity> getTransactions(int page, int size, String sort, String search);
}
