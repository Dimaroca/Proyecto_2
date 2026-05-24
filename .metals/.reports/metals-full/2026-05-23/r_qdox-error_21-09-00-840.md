error id: file:///C:/Users/Jair%20Morales/OneDrive%20-%20UVG/Escritorio/Progra2/Proyecto_2/src/main/java/dataset/Main.java
file:///C:/Users/Jair%20Morales/OneDrive%20-%20UVG/Escritorio/Progra2/Proyecto_2/src/main/java/dataset/Main.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file:///C:/Users/Jair%20Morales/OneDrive%20-%20UVG/Escritorio/Progra2/Proyecto_2/src/main/java/dataset/Main.java
text:
```scala
a@@ckage dataset;

/**
 * Main class of the program.
 * Initializes the connection with Neo4j
 * and loads the restaurant dataset.
 */
public class Main {

    /**
     * Main method of the program.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {

        String uri = "neo4j://127.0.0.1:7687";
        String user = "neo4j";
        String password = "12345678";

        try (Neo4jManager neo4j =
                     new Neo4jManager(uri, user, password)) {

            /*
             * Verifies if the connection to Neo4j
             * was established successfully.
             */
            if (neo4j.testConnection()) {

                /*
                 * Loads the dataset into the graph database.
                 */
                neo4j.loadDataset();

                System.out.println("OK");
            }

            /*
             * Displays an error message if the
             * connection to Neo4j fails.
             */
            else {

                System.out.println("Conexión fallida");

                System.out.println(
                        "Verifique que Neo4j esté encendido y que las credenciales sean correctas"
                );
            }
        }

        /*
         * Handles unexpected execution errors.
         */
        catch (Exception e) {

            System.out.println(
                    "Error inesperado: " + e.getMessage()
            );
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:940)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

QDox parse error in file:///C:/Users/Jair%20Morales/OneDrive%20-%20UVG/Escritorio/Progra2/Proyecto_2/src/main/java/dataset/Main.java