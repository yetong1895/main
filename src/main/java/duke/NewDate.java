package duke;
import com.joestelmach.natty.*;

import java.util.List;

public class NewDate {
    private String split[] = new String[100];




for(DateGroup group:groups) {
        List dates = group.getDates();
        int line = group.getLine();
        int column = group.getPosition();
        String matchingValue = group.getText();
        String syntaxTree = group.getSyntaxTree().toStringTree();
        Map> parseMap = group.getParseLocations();
        boolean isRecurreing = group.isRecurring();
        Date recursUntil = group.getRecursUntil();
    }
}
