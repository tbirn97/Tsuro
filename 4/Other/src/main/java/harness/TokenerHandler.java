package harness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class TokenerHandler {

  /**
   * Get the next JSONArray from a JSONTokener.
   */
  public static JSONArray getNextJSONArray(JSONTokener tokener) {
    while (tokener.more()) {
      Object nextObject = tokener.nextValue();
      if (nextObject instanceof JSONArray) {
        JSONArray nextArray = (JSONArray) nextObject;

        if (tokener.nextClean() != 0) {
          tokener.back();
        }

        return nextArray;
      }
    }

    throw new JSONException("Given a tokener that does not have any more inputs to parse");
  }
}
