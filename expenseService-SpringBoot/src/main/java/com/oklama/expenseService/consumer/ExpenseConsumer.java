package com.oklama.expenseService.consumer;

import com.oklama.expenseService.dto.ExpenseDto;
import com.oklama.expenseService.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseConsumer {
    private ExpenseService expenseService;

    @Autowired
    ExpenseConsumer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic-json.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "${spring.kafka.consumer.enabled:true}"
    )
    public void listen(ExpenseDto eventData){
        try{
            expenseService.createExpense(eventData);
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
        }
    }
}
