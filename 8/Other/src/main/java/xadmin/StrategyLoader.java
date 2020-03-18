package xadmin;

import src.Player.Strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StrategyLoader {

  /**
   * Dynamically load a strategy object from a file path string.
   */
  public static Strategy getStrategyFromFilePath(String path) {
    String packageName = getPackageFromFilePath(path);

    try {
      Class<?> clazz = Class.forName(packageName);
      Class<? extends Strategy> strategyClazz = clazz.asSubclass(Strategy.class);
      Constructor<? extends Strategy> strategyConstructor = strategyClazz.getConstructor();
      return strategyConstructor.newInstance();
    } catch (ClassNotFoundException |
        NoSuchMethodException |
        InstantiationException |
        IllegalAccessException |
        InvocationTargetException e) {
      throw new IllegalArgumentException("Could not load strategy at: " + path, e);
    }
  }

  /**
   * Convert a file path into a package name. Assume that package names begin with "src" based
   * on our file structure.
   */
  private static String getPackageFromFilePath(String filePath) {
    Path path = Paths.get(filePath);

    List<String> packageList = new ArrayList<>();
    String firstFile = path.getFileName().toString().split("\\.")[0];
    packageList.add(firstFile);
    path = path.getParent();

    while (path.getNameCount() > 0) {
      String nextFile = path.getFileName().toString();
      packageList.add(nextFile);
      if (nextFile.equals("src")) {
        break;
      }
      path = path.getParent();
    }

    StringBuilder packageName = new StringBuilder();
    for (int i = packageList.size() - 1; i >= 0; i--) {
      packageName.append(packageList.get(i));
      if (i > 0) {
        packageName.append(".");
      }
    }
    return packageName.toString();
  }
}
