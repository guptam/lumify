package io.lumify.mapping.column;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.lumify.mapping.column.AbstractColumnDocumentMapping.Row;
import java.util.Map;
import org.securegraph.Vertex;

/**
 * Base implementation of ColumnRelationshipMapping.
 */
public abstract class AbstractColumnRelationshipMapping implements ColumnRelationshipMapping {
    /**
     * The source entity key.
     */
    private final String sourceKey;

    /**
     * The target entity key.
     */
    private final String targetKey;

    /**
     * Create a new AbstractColumnRelationshipMapping.
     *
     * @param srcKey the source entity key
     * @param tgtKey the target entity key
     */
    protected AbstractColumnRelationshipMapping(final String srcKey, final String tgtKey) {
        checkNotNull(srcKey, "source key must be provided");
        checkArgument(!srcKey.trim().isEmpty(), "source key must be provided");
        checkNotNull(tgtKey, "target key must be provided");
        checkArgument(!tgtKey.trim().isEmpty(), "target key must be provided");
        this.sourceKey = srcKey;
        this.targetKey = tgtKey;
    }

    @JsonProperty("source")
    public final String getSourceKey() {
        return sourceKey;
    }

    @JsonProperty("target")
    public final String getTargetKey() {
        return targetKey;
    }

    /**
     * Get the label for the relationship generated by this key.
     * @param source the source entity
     * @param target the target entity
     * @param row    the current row
     * @return the relationship label or <code>null</code> if it cannot be determined
     */
    protected abstract String getLabel(final Vertex source, final Vertex target, final Row row);

    @Override
    public RelationshipDef defineRelationship(final Map<String, Vertex> entities, final Row row) {
        RelationshipDef relDef = null;
        if (entities != null) {
            Vertex source = entities.get(sourceKey);
            Vertex target = entities.get(targetKey);
            if (source != null && target != null) {
                String label = getLabel(source, target, row);
                if (label != null && !label.trim().isEmpty()) {
                    relDef = new RelationshipDef(label, source, target);
                }
            }
        }
        return relDef;
    }
}