package com.example.polzunovfeastserver.route.node;

import com.example.polzunovfeastserver.event.EventEntity;
import com.example.polzunovfeastserver.route.node.util.RouteNodesTableKeys;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "route_nodes")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RouteNodeEntity {

    @Id
    @Column(name = "route_node_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "event_id",
            foreignKey = @ForeignKey(name = RouteNodesTableKeys.FOREIGN_EVENT, value = ConstraintMode.CONSTRAINT)
    )
    private EventEntity event;
}
