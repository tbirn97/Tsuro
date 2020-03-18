# Dynamic Code Loading

StrategyLoader in `8/Other/src/main/java/xadmin/StrategyLoader.java` handles the dynamic loading of a Strategy instance from a file path.

In Java, code can be dynamically loaded with the Java Class Loader. The class loader is a part of the Java Runtime Environment (the runner that can execute Java code) and can dynamically load java classes into the Java Virtual Machine (the environment that the JRE is constructing). The class loader abstracts away from file names and only loads classes by their package name. Package names are similar to file paths but only begin from the root of the Java source files. To convert a file path into a package name, we need to be able to assume which directory is a source file root. We are able to do this because all of our package names begin with `src`. Additionally, package names do not include file extensions so these must be removed from a file path.

Once we have a package name we are able to load a class with `Class.forName`. This Class<?> object can then be casted into a strategy object and instantiated with its default constructor.

This approach is limited in that the class we want to load has to be available to our class loader meaning that any class we want to load has to be declared a dependency beforehand. We can not load arbitrary files that exist on our hard drives.
