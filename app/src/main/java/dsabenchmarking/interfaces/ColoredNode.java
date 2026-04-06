package dsabenchmarking.interfaces;

import dsabenchmarking.enumeration.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public abstract class ColoredNode {
    ColoredNode left, right, parent;
    Color color;
    int value;
}
