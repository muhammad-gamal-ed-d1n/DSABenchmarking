package dsabenchmarking.interfaces;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Node {
    Node left, right, parent;
}
