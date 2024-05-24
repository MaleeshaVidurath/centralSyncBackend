package CentralSync.demo.util;

import CentralSync.demo.model.ItemGroupEnum;

import java.util.Map;
import java.util.Set;

public class ItemGroupUnitMapping {

    public static final Map<ItemGroupEnum, Set<String>> VALID_UNITS = Map.of(
            ItemGroupEnum.COMPUTER_ACCESSORIES, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.PRINTER, Set.of("pcs", "units"),
            ItemGroupEnum.COMPUTER_HARDWARE, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.OTHER, Set.of("pcs", "boxes", "units", "kg")
    );
}
