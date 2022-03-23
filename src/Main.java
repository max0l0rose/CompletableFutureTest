import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        System.out.println(0);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(1);
                    return "Hello";
                }).thenCombine(CompletableFuture.supplyAsync(() -> { // parallel
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(2);
                    return " World";
                }), (s1, s2) -> s1 + s2);

        String s = completableFuture.get();
        System.out.println(s);
        int a = 1;


//        Future<String> completableFuture =
//                CompletableFuture.completedFuture("Hello");
//
//        String result = completableFuture.get();
//        assertEquals("Hello", result);



//        CompletableFuture<String> future1
//                = CompletableFuture.supplyAsync(() -> "Hello");
//        CompletableFuture<String> future2
//                = CompletableFuture.supplyAsync(() -> "Beautiful");
//        CompletableFuture<String> future3
//                = CompletableFuture.supplyAsync(() -> "World");
//
//        CompletableFuture<Void> combinedFuture
//                = CompletableFuture.allOf(future1, future2, future3);
//
//        combinedFuture.get();

    }
}
