package CentralSync.demo.util;

import CentralSync.demo.model.ItemGroupEnum;

import java.util.Map;
import java.util.Set;

public class ItemGroupUnitMapping {

    public static final Map<ItemGroupEnum, Set<String>> VALID_UNITS = Map.of(
            ItemGroupEnum.COMPUTERS_AND_LAPTOPS, Set.of("pcs","units"),
            ItemGroupEnum.COMPUTER_ACCESSORIES, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.COMPUTER_HARDWARE, Set.of("pcs", "boxes", "units"),
            ItemGroupEnum.OFFICE_SUPPLIES, Set.of("pcs", "boxes","kg"),
            ItemGroupEnum.FURNITURE, Set.of("pcs",  "units"),
            ItemGroupEnum.PRINTERS_AND_SCANNERS, Set.of("pcs", "units"),
            ItemGroupEnum.OTHER, Set.of("pcs", "boxes", "units", "kg")
    );
}
