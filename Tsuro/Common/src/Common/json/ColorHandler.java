package src.Common.json;

import org.json.JSONArray;
import src.Common.AvatarColor;

import java.util.ArrayList;
import java.util.List;

public class ColorHandler {

  /**
   * Given a json array of colors, turn that into a list of avatarColors
   * @param jsonColors ["BLUE", "RED", "WHITE"], for example
   * @return
   */
  public static List<AvatarColor> jsonToList(JSONArray jsonColors) {
    ArrayList<AvatarColor> colors = new ArrayList<>();
    for(Object o : jsonColors.toList()) {
      AvatarColor color = AvatarColor.valueOf(o.toString());
      colors.add(color);
    }
    return colors;
  }

  /**
   * Given a list of AvatarColor convert that into a json array
   * @param avatarColors a json array
   * @return
   */
  public static JSONArray listToJson(List<AvatarColor> avatarColors) {
    JSONArray colorsArray = new JSONArray();
    for(AvatarColor color : avatarColors) {
      colorsArray.put(color);
    }
    return colorsArray;
  }
}
