package org.hamnett.adm.util;

import java.util.HashMap;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

public class DazzleYamlConstructor extends Constructor {
    private HashMap<String,Class<?>> classMap = new HashMap<String,Class<?>>();

    public DazzleYamlConstructor(Class<? extends Object> theRoot) {
        super( theRoot);
        classMap.put( YamlPrefs.class.getName(), YamlPrefs.class );
        classMap.put( LWRFDevice.class.getName(), LWRFDevice.class );
    }

    /*
     * This is a modified version of the Constructor. Rather than using a class loader to
     * get external classes, they are already predefined above. This approach works similar to
     * the typeTags structure in the original constructor, except that class information is
     * pre-populated during initialization rather than runtime.
     *
     * @see org.yaml.snakeyaml.constructor.Constructor#getClassForNode(org.yaml.snakeyaml.nodes.Node)
     */
     protected Class<?> getClassForNode(Node node) {
         String name = node.getTag().getClassName();
         Class<?> cl = classMap.get( name );
         if ( cl == null )
             throw new YAMLException( "Class not found: " + name );
         else
             return cl;
     }
}
