package ${packageName};

import jakarta.persistence.*;
import lombok.*;
<% if (sql) { %>import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;<% } %>

@Entity
<% if (sql) { %>@Immutable
@Subselect("${sql}")
<% } else { %>@Table(name = "${tableName}")<% } %>
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${className} {
<% attributes.each { attr -> %><% if (attr.isId) { %>
    @Id
<% if (!sql) { %>    @GeneratedValue(strategy = GenerationType.IDENTITY)<% } %>
    private ${attr.type} ${attr.name};
<% } else { %>
    private ${attr.type} ${attr.name};
<% } %><% } %>
}
