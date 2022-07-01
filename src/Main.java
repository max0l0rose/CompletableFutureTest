import java.util.concurrent.*;

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

        Object object = new Object();


        System.out.println(0);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                        //object.wait(2000);
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



    static ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "dead-pool")); //r -> new Thread(r, "dead-pool")
    static Executor es = Runnable::run; // doesnt work


    // !!!!!!!!!!!!!!!!!!!! Current Thread Executor !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    static class CurrentThreadExecutor implements Executor {
        public void execute(Runnable r) {
            r.run();
        }
    }


    public static void main(String[] args)
    {
        //thenCombine_test();
        //thenApply_test();

        int a = 1;



//        CompletableFuture<Void> completableFuture =
//                CompletableFuture.completedFuture("Hello").thenAccept((v)->{
//                    System.out.println(v);
//                });

        //String result = completableFuture.get();
        //assertEquals("Hello", result);


        Executor currentThreadExecutor = new CurrentThreadExecutor();

        System.out.println("Begin");
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(//.completedFuture(1
                () ->  {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("supplyAsync " + Thread.currentThread().getName());
            return 1;
        }, currentThreadExecutor//, executorService
        );
        CompletableFuture cf3 = cf2.thenApplyAsync( //thenAcceptAsync( // no res
                    v -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("thenApplyAsync " + v + ", " + Thread.currentThread().getName());
                        //int q = 1/0;
                        return 2;
                    }, currentThreadExecutor //, executorService
            )
            .exceptionally(ex -> {
                System.out.println("exceptionally " + ex + ", " + Thread.currentThread().getName());
                return 3;
           })
//                .thenAcceptBoth(CompletableFuture.completedFuture(1) //.supplyAsync(() -> " World"),
//                        , (s1, s2) -> {
//                            int qq = 1;
//                            //return null;
//                        }
//                )
                .thenCombine(CompletableFuture.supplyAsync(()-> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return 10;
                    }
                    , currentThreadExecutor
                    ) //.supplyAsync(() -> " World"),
                    , (s1, s2) -> {
                    return s1 + s2;
                    }
                )
                .thenCompose(q -> CompletableFuture.completedFuture(q+1))
                ;

        //cf3.handle()
//        try {
//            cf3.join();
//        } catch (CompletionException e) {
//            e.printStackTrace();
//        }

//        try {
//            cf3.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//        System.out.println("Joined ");

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("End " + cf3.isDone() + " " + cf3.isCompletedExceptionally());


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
