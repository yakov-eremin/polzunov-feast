package com.example.polzunovfeastserver.route;

import com.example.polzunovfeastserver.route.node.RouteNodeEntity;
import com.example.polzunovfeastserver.route.node.util.RouteNodesTableKeys;
import com.example.polzunovfeastserver.route.util.RoutesTableKeys;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(
        name = "routes",
        uniqueConstraints = {
                @UniqueConstraint(name = RoutesTableKeys.UNIQUE_OWNER, columnNames = "owner_id"),
        }
)
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RouteEntity {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = RoutesTableKeys.FOREIGN_OWNER, value = ConstraintMode.CONSTRAINT)
    )
    private UserEntity owner;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(
            name = "route_id",
            foreignKey = @ForeignKey(name = RouteNodesTableKeys.FOREIGN_ROUTE, value = ConstraintMode.CONSTRAINT)
    )
    private List<RouteNodeEntity> nodes;

    public void updateNodes(List<RouteNodeEntity> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }
}
