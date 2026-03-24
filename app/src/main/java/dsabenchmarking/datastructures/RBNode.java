package dsabenchmarking.datastructures;

import dsabenchmarking.enumeration.Color;
import dsabenchmarking.interfaces.ColoredNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RBNode extends ColoredNode {
    private ColoredNode left, right, parent;
    private int value;
    private Color color;
}
