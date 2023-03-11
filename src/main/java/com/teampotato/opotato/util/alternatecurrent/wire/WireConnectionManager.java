package com.teampotato.opotato.util.alternatecurrent.wire;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class WireConnectionManager {

	/** The owner of these connections. */
	final WireNode owner;

	/** The first connection for each cardinal direction. */
	private final WireConnection[] heads;

	private WireConnection head;
	private WireConnection tail;

	/** The total number of connections. */
	int total;

	/**
	 * A 4 bit number that encodes which in direction(s) the owner has connections
	 * to other wires.
	 */
	private int flowTotal;
	/** The direction of flow based connections to other wires. */
	int iFlowDir;

	WireConnectionManager(WireNode owner) {
		this.owner = owner;

		this.heads = new WireConnection[WireHandler.Directions.HORIZONTAL.length];

		this.total = 0;

		this.flowTotal = 0;
		this.iFlowDir = -1;
	}

	void set(WireHandler.NodeProvider nodes) {
		if (total > 0) clear();

		boolean belowIsConductor = nodes.getNeighbor(owner, WireHandler.Directions.DOWN).isConductor();
		boolean aboveIsConductor = nodes.getNeighbor(owner, WireHandler.Directions.UP).isConductor();

		for (int iDir = 0; iDir < WireHandler.Directions.HORIZONTAL.length; iDir++) {
			Node neighbor = nodes.getNeighbor(owner, iDir);

			if (neighbor.isWire()) {
				add(neighbor.asWire(), iDir, true, true);

				continue;
			}

			boolean sideIsConductor = neighbor.isConductor();

			if (!sideIsConductor) {
				Node node = nodes.getNeighbor(neighbor, WireHandler.Directions.DOWN);

				if (node.isWire()) add(node.asWire(), iDir, belowIsConductor, true);
			}
			if (!aboveIsConductor) {
				Node node = nodes.getNeighbor(neighbor, WireHandler.Directions.UP);

				if (node.isWire()) add(node.asWire(), iDir, true, sideIsConductor);

			}
		}
		if (total > 0) iFlowDir = WireHandler.FLOW_IN_TO_FLOW_OUT[flowTotal];
	}

	private void clear() {
		Arrays.fill(heads, null);

		head = null;
		tail = null;

		total = 0;

		flowTotal = 0;
		iFlowDir = -1;
	}

	private void add(WireNode wire, int iDir, boolean offer, boolean accept) {
		add(new WireConnection(wire, iDir, offer, accept));
	}

	private void add(WireConnection connection) {
		if (head == null) {
			head = connection;
		} else {
			tail.next = connection;
		}
		tail = connection;

		total++;

		if (heads[connection.iDir] == null) {
			heads[connection.iDir] = connection;
			flowTotal |= (1 << connection.iDir);
		}
	}


	/**
	 * Iterate over all connections. Use this method if the iteration order is not
	 * important.
	 */
	void forEach(Consumer<WireConnection> consumer) {
		for (WireConnection c = head; c != null; c = c.next) {
			consumer.accept(c);
		}
	}

	/**
	 * Iterate over all connections. Use this method if the iteration order is
	 * important.
	 */
	void forEach(Consumer<WireConnection> consumer, int iFlowDir) {
		IntStream.of(WireHandler.CARDINAL_UPDATE_ORDERS[iFlowDir])
				.mapToObj(iDir -> heads[iDir])
				.filter(Objects::nonNull)
				.forEach(consumer);
	}
}
