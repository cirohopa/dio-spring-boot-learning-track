package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class CalculateTotalExpensesUseCase {
    private final TransactionRepository transactionRepository;

    public CalculateTotalExpensesUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "calculate-total-expenses", description = "Calcula e retorna a soma do valor (amount) de todas as despesas/transações financeiras registradas. Use esta ferramenta quando o usuário quiser saber o gasto total.")
    public long execute() {
        return transactionRepository.findAll().stream()
                .mapToLong(Transaction::getAmount)
                .sum();
    }
}
