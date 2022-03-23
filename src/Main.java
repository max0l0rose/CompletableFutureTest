import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    // parallel execution
    static void thenCombine_test() throws ExecutionException, InterruptedException {
        //ExecutorService executorService = Executors.newSingleThreadExecutor(); //r -> new Thread(r, "dead-pool")

        System.out.println(0);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(1 + " " + Thread.currentThread().getName());
                    return "Hello";
                })
            .thenCombineAsync(CompletableFuture.supplyAsync(() -> { // parallel
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(2 + " " + Thread.currentThread().getName());
                    return " World";
                }), (s1, s2) -> s1 + s2
            );

        String s = completableFuture.get();
        System.out.println(s);
    }

    // sequential execution
    static void thenApply_test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "dead-pool")); //r -> new Thread(r, "dead-pool")

        Object object = new Object();


        System.out.println(0);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                        object.wait(2000);
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(1 + " " + Thread.currentThread().getName());
                        return "Hello";
                    }, executorService)
                .thenApplyAsync(q -> { // parallel
                                        try {
                                            Thread.sleep(2000);
                                            //Thread.currentThread().interrupt();
                                            //throw new IllegalStateException("qqqqq");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println(2 + " " + Thread.currentThread().getName());
                                        return q + " World";
                                    })
                .handle((s, t) -> s != null ? s + "eee" : "Hello, s is NULL!"); //, (s1, s2) -> s1 + s2

        String s = completableFuture.get();
        System.out.println(s);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        //thenCombine_test();
        thenApply_test();

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
