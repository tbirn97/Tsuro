package src.Common.json;

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

  /**
   * Get the next JSONArray from a JSONTokener.
   */
  public static JSONArray getNextJSONArrayNonContinuous(JSONTokener tokener) {
    while (tokener.more()) {
      Object nextObject = tokener.nextValue();
      if (nextObject instanceof JSONArray) {
        JSONArray nextArray = (JSONArray) nextObject;

        return nextArray;
      }
    }

    throw new JSONException("Given a tokener that does not have any more inputs to parse");
  }


  public static String getNextJSONStringNonContinuous(JSONTokener tokener) {
    while (tokener.more()) {
      Object nextObject = tokener.nextValue();
      if (nextObject instanceof String) {
        String msg = (String) nextObject;

        return msg;
      }
    }
    throw new JSONException("Given a tokener that does not have any more inputs to parse");
  }
}
