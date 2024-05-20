package CentralSync.demo.util;

import CentralSync.demo.model.ItemGroupEnum;

import java.util.Map;
import java.util.Set;

public class ItemGroupUnitMapping {

    public static final Map<ItemGroupEnum, Set<String>> VALID_UNITS = Map.of(
            ItemGroupEnum.computerAccessories, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.printer, Set.of("pcs", "units"),
            ItemGroupEnum.computerHardware, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.other, Set.of("pcs", "boxes", "units", "kg")
    );
}
