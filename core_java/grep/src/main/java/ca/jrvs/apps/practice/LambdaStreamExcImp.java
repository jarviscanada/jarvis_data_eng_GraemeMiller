package ca.jrvs.apps.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc{

    public static void main(String[] args) {
        LambdaStreamExc lse = new LambdaStreamExcImp();
        Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
        printer.accept("Message body");
        String[] messages = {"a", "b", "c"};
        lse.printMessages(messages, lse.getLambdaPrinter("msg:", "!") );
        lse.printOdd(lse.createIntStream(0,5), lse.getLambdaPrinter("odd number:", "!"));
    }

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        for (int i = 0; i< strings.length; i++) {
            strings[i] = strings[i].toUpperCase();
        }
        return createStrStream(strings);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> !s.contains(pattern));
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        return Arrays.stream(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(i -> Math.sqrt(i));
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(i -> i % 2 == 1);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return (message) -> System.out.println(prefix + message + suffix);
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Arrays.stream(messages).forEach(printer);
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream).mapToObj(Integer::toString).forEach(printer);
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        // ints.flatMapToInt(list -> list.stream().mapToInt(Integer::intValue)).boxed(); ends up as an intStream
        return ints.flatMap(List::stream);
    }
}
