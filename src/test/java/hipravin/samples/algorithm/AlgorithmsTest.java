package hipravin.samples.algorithm;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmsTest {
    final Random r = new Random();
    final List<String> expectedAfterRemove = testArrayListOneTo10DoubledOdd();

    @Test
    void testRemoveFromListCasual() {
        List<String> elements = testArrayListOneTo10Doubled();

        elements.removeIf(s -> Long.parseLong(s) % 2 == 1);
    }

    @Test
    void testRemoveFromListCasualBeforeJava8() {
        List<String> elements = testArrayListOneTo10Doubled();

        for (Iterator<String> iterator = elements.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if (Long.parseLong(next) % 2 == 1) {
                iterator.remove();
            }
        }
    }

    @Test
    void testRemoveFromListVar1BeforeJava8() {
        List<String> elements = testArrayListOneTo10Doubled();

        int counter = 0;
        for (Iterator<String> iterator = elements.iterator(); iterator.hasNext(); ) {
            iterator.next();
            if (counter++ % 2 == 1) {
                iterator.remove();
            }
        }
        assertEquals(expectedAfterRemove, elements);
    }

    @Test
    void testRemoveFromListVar1BeforeJava8Tricky() {
        List<String> elements = testArrayListOneTo10Doubled();

        for (ListIterator<String> literator = elements.listIterator(elements.size()); literator.hasPrevious(); ) {
            literator.previous();
            if (literator.previousIndex() % 2 == 0) {
                literator.remove();
            }
        }
        assertEquals(expectedAfterRemove, elements);
    }

    @Test
    void testRemoveFromListJava8() {
        List<String> elements = testArrayListOneTo10Doubled();

        AtomicInteger counter = new AtomicInteger(0);
        elements.removeIf(s -> counter.incrementAndGet() % 2 == 0);

        assertEquals(expectedAfterRemove, elements);
    }

    @Test
    void testRemoveFromArrayListOn() {
        //O(n) implementation
        ArrayList<String> elements = testArrayListOneTo10Doubled();

        removeFromArrayList(elements, i -> i % 2 == 1);

        assertEquals(expectedAfterRemove, elements);
    }

    @Test
    void stackOverflow1Correect() {
        List<String> words = testArrayListOneTo10Doubled();

        int i = 0;
        for (Iterator<String> it = words.iterator(); it.hasNext(); )
        {
            it.next(); // Add this line in your code
            if (i % 2 != 0)
            {
                it.remove();
            }
            i++;
        }

        assertEquals(expectedAfterRemove, words);
    }

    @Test
    void stackOverflow2Incorrect() {
        List<String> words = testArrayListOneTo10Doubled();

        int i = 0;
        List<String> list = new ArrayList<String>();
        for (String word:words)
        {
            if (i % 2 != 0)
            {
                //it.remove();
                list.add(word);
            }

            i++;
        }
//        words.removeAll(list);
        words.clear();
        words.addAll(list);

        assertEquals(expectedAfterRemove, words);
    }

    @Test
    void stackOverflow3Correct() {
        List<String> words = testArrayListOneTo10Doubled();

        int i = 1;

        while (i < words.size()) {
            System.out.println(i + " " + words.get(i));
            words.remove(i++);
        }

        assertEquals(expectedAfterRemove, words);
    }

    @Test
    void stackOverflow4Correct() {
        List<String> integers = testArrayListOneTo10Doubled();


            int j = 0;
            for(int i = 0 ; i < integers.size(); i++){
                if( i % 2 == 0){
                    integers.set(j, integers.get(i));
                    j++;
                }
            }
            int half = integers.size()%2==0 ? integers.size()/2 : integers.size()/2 + 1;
            integers.subList(half , integers.size()).clear();

        assertEquals(expectedAfterRemove, integers);
    }

    private static void removeFromArrayList(ArrayList<String> elements, IntPredicate removeIf) {
        int lastIndex = 0;
        for (int i = 0; i < elements.size(); i++) {
            if (!removeIf.test(i)) {
                elements.set(lastIndex, elements.get(i));
                lastIndex++;
            }
        }
        int extraElementsCount = elements.size() - lastIndex;
        //unfortunately, we can't use protected removeRange method
        for (int i = 0; i < extraElementsCount; i++) {
            elements.remove(elements.size() - 1);
        }
    }


    ArrayList<String> testArrayListOneTo10Doubled() {
        ArrayList<String> elements = IntStream.range(0, 10).mapToObj(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
        elements.add("100");
        elements.addAll(new ArrayList<>(elements));//double list to create repeated values

        return elements;
    }

    List<String> testArrayListOneTo10DoubledOdd() {
        List<String> elements = testArrayListOneTo10Doubled();
        AtomicInteger counter = new AtomicInteger(0);
        elements.removeIf(s -> counter.incrementAndGet() % 2 == 0);

        return elements;
    }

    @Test
    void testDistributionOfRandomArrayByPivot() {
        //random array
        List<Long> list = randomLongLilst(10000);
        long totalMin = 0;
        long totalMax = 0;

        int tries = 1000;
        for (int i = 0; i < tries; i++) {
            long pivot = list.get(r.nextInt(list.size()));

            long lte = list.stream().filter(v -> v < pivot).count();
            long gte = list.stream().filter(v -> v > pivot).count();

            totalMin += Math.min(lte, gte);
            totalMax += Math.max(lte, gte);

        }
        System.out.println((double) totalMax / totalMin);//should be around 3

    }

    private List<Long> randomLongLilst(int size) {
        return LongStream.generate(r::nextLong)
                .limit(size)
                .boxed()
                .collect(Collectors.toList());
    }


}