package SupportLibraries;
 
import java.util.Map;
 
import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;
 
public class CrazyCapabilityMatcher extends DefaultCapabilityMatcher {
    private final String NodeName = "NodeName";
    @Override
    public boolean matches(Map<String, Object> nodeCapability, Map<String, Object> requestedCapability) {
        boolean basicChecks = super.matches(nodeCapability, requestedCapability);
        if (! requestedCapability.containsKey(NodeName)){
            //If the user didnt set the custom capability lets just return what the DefaultCapabilityMatcher
            //would return. That way we are backward compatibility and arent breaking the default behavior of the
            //grid
            return basicChecks;
        }
        return (basicChecks && nodeCapability.get(NodeName).equals(requestedCapability.get(NodeName)));
    }
 
}