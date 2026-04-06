package dsabenchmarking.datastructures;

import dsabenchmarking.interfaces.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BSTNode extends Node {
    private Node left, right, parent;
    int value;
}
