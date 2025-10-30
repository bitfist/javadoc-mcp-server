package io.github.bitfist.javadoc_mcp_server.javadoc.internal.platform.graalvm;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import com.vladsch.flexmark.util.misc.BitFieldSet;

import java.util.concurrent.ConcurrentHashMap;


@TargetClass(value = BitFieldSet.class, innerClass = "UniverseLoader")
final class FlexmarkUniverseLoader {

    @Alias
    static ConcurrentHashMap<Class, Enum[]> enumUniverseMap;

    /**
     * Provides a reflection-free substitute implementation for retrieving enum universes in a GraalVM
     * native image environment. This class is designed to replace the standard universe loader functionality
     * within BitFieldSet. The method retrieves enum constants from a given enum class and caches them for
     * future use, ensuring efficient access to enum universes without redundant computation.
     *
     * @param elementType the class of the enum type to retrieve universes for
     * @return an array of enum constants for the specified enum type
     * @throws AssertionError if the provided class is not an enum type
     */
    @Substitute
    public static <E extends Enum<E>> Enum[] getUniverseSlow(Class<E> elementType) {
        assert (elementType.isEnum());
        Enum[] cachedUniverse = enumUniverseMap.get(elementType);
        if (cachedUniverse != null) return cachedUniverse;

        E[] constants = elementType.getEnumConstants();
        int enums = constants.length;
        if (enums > 0) {
            cachedUniverse = new Enum[enums];

            enums = 0;
            for (E constant : constants) {
                cachedUniverse[enums++] = constant;
            }
        } else {
            cachedUniverse = new Enum<?>[0];
        }

        enumUniverseMap.put(elementType, cachedUniverse);
        return cachedUniverse;
    }
}
