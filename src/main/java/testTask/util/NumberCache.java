package testTask.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class NumberCache {
    @Value("${cards.number.cache.batch}")
    private int batch;
    @Value("${cards.number.cache.min}")
    private int min;
    @Value("${cards.number.cache.capacity}")
    private int capacity;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private BlockingQueue<String> numbers;

    private final CardGenerator generator;

    @PostConstruct
    public void init() {
        numbers = new ArrayBlockingQueue<>(capacity);
        numbers.addAll(generator.generateCardNumbers(capacity));
    }

    @Transactional
    public String getNumber() {
        if(numbers.size() < min && isRunning.compareAndSet(false, true)) {
            fillNumberCache();
        }
        return numbers.poll();
    }

    @Async
    public void fillNumberCache() {
        List<String> toAdd = generator.generateCardNumbers(batch);
        this.numbers.addAll(toAdd);
    }
}
