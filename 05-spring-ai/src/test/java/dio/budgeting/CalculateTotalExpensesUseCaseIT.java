package dio.budgeting;

import dio.budgeting.application.CalculateTotalExpensesUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.infrastructure.persistence.repository.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class CalculateTotalExpensesUseCaseIT {
    @Autowired
    OpenAiChatModel openAiChatModel;

    @Autowired
    CalculateTotalExpensesUseCase calculateTotalExpensesUseCase;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionEntityRepository transactionEntityRepository;

    @BeforeEach
    void setUp() {
        transactionEntityRepository.deleteAll();
        
        transactionRepository.save(new Transaction("Compra 1", 100, Category.GROCERIES));
        transactionRepository.save(new Transaction("Compra 2", 250, Category.AUTO));
    }

    @Test
    void should_calculateTotalExpenses_when_prompted() {
        var chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(calculateTotalExpensesUseCase)
                .build();

        var response = chatClient.prompt("Qual é o valor total exato de todas as minhas despesas cadastradas? Exiba apenas o número final do resultado, sem explicações extras.")
                .call().content();

        // 100 + 250 = 350
        assertThat(response).contains("350");
        System.out.println("Resposta da IA: " + response);
    }
}
