package dsabenchmarking.interfaces;

import dsabenchmarking.enumeration.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ColoredNode {
    ColoredNode left, right, parent;
    Color color;
    int value;
}
