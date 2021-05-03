package com.brewmes.common.util.node_enums;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public enum CommandNodes {
    SET_MACHINE_SPEED(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.MachSpeed")),
    SET_MACHINE_COMMAND(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.CntrlCmd")),
    EXECUTE_MACHINE_COMMAND(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.CmdChangeRequest")),
    SET_NEXT_BATCH_ID(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.Parameter[0].Value")),
    SET_PRODUCT_ID_FOR_NEXT_BATCH(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.Parameter[1].Value")),
    SET_PRODUCT_AMOUNT_IN_NEXT_BATCH(new NodeId(Constants.NAMESPACE_INDEX_VALUE, "::Program:Cube.Command.Parameter[2].Value"));

    public final NodeId nodeId;

    CommandNodes(NodeId nodeId) {
        this.nodeId = nodeId;
    }
}
