CREATE TABLE infrastructure_nodes (
 id BINARY(16) PRIMARY KEY, code VARCHAR(40) NOT NULL UNIQUE, name VARCHAR(160) NOT NULL,
 type VARCHAR(32) NOT NULL, latitude DECIMAL(9,6) NOT NULL, longitude DECIMAL(9,6) NOT NULL,
 demand DECIMAL(14,2) NOT NULL DEFAULT 0, supply DECIMAL(14,2) NOT NULL DEFAULT 0,
 description TEXT, sample_data BOOLEAN NOT NULL DEFAULT FALSE, version BIGINT NOT NULL DEFAULT 0,
 created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6), updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 INDEX idx_nodes_type(type), INDEX idx_nodes_sample(sample_data)
);
CREATE TABLE infrastructure_edges (
 id BINARY(16) PRIMARY KEY, node_a_id BINARY(16) NOT NULL, node_b_id BINARY(16) NOT NULL,
 endpoint_low_id BINARY(16) NOT NULL, endpoint_high_id BINARY(16) NOT NULL,
 distance_km DECIMAL(12,3) NOT NULL, construction_cost DECIMAL(16,2) NOT NULL, capacity DECIMAL(14,2) NOT NULL DEFAULT 0,
 status VARCHAR(16) NOT NULL, sample_data BOOLEAN NOT NULL DEFAULT FALSE, version BIGINT NOT NULL DEFAULT 0,
 created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6), updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 CONSTRAINT fk_edge_a FOREIGN KEY(node_a_id) REFERENCES infrastructure_nodes(id) ON DELETE CASCADE,
 CONSTRAINT fk_edge_b FOREIGN KEY(node_b_id) REFERENCES infrastructure_nodes(id) ON DELETE CASCADE,
 CONSTRAINT uq_edge_pair UNIQUE(endpoint_low_id,endpoint_high_id),
 CONSTRAINT ck_edge_distance CHECK(distance_km > 0), CONSTRAINT ck_edge_cost CHECK(construction_cost >= 0),
 INDEX idx_edges_status(status), INDEX idx_edges_a(node_a_id), INDEX idx_edges_b(node_b_id)
);
CREATE TABLE network_revisions (
 id BINARY(16) PRIMARY KEY, revision_number BIGINT NOT NULL UNIQUE AUTO_INCREMENT, reason VARCHAR(160) NOT NULL,
 created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);
CREATE TABLE network_revision_nodes (
 revision_id BINARY(16) NOT NULL, id BINARY(16) NOT NULL, code VARCHAR(40) NOT NULL, name VARCHAR(160) NOT NULL,
 type VARCHAR(32) NOT NULL, latitude DECIMAL(9,6) NOT NULL, longitude DECIMAL(9,6) NOT NULL,
 demand DECIMAL(14,2) NOT NULL, supply DECIMAL(14,2) NOT NULL, description TEXT,
 PRIMARY KEY(revision_id,id), FOREIGN KEY(revision_id) REFERENCES network_revisions(id) ON DELETE CASCADE
);
CREATE TABLE network_revision_edges (
 revision_id BINARY(16) NOT NULL, id BINARY(16) NOT NULL, node_a_id BINARY(16) NOT NULL, node_b_id BINARY(16) NOT NULL,
 distance_km DECIMAL(12,3) NOT NULL, construction_cost DECIMAL(16,2) NOT NULL, capacity DECIMAL(14,2) NOT NULL, status VARCHAR(16) NOT NULL,
 PRIMARY KEY(revision_id,id), FOREIGN KEY(revision_id) REFERENCES network_revisions(id) ON DELETE CASCADE
);
CREATE TABLE optimization_runs (
 id BINARY(16) PRIMARY KEY, revision_id BINARY(16) NOT NULL, algorithm VARCHAR(16) NOT NULL, metric VARCHAR(16) NOT NULL,
 selected_edges_json JSON NOT NULL, result_json JSON NOT NULL, name VARCHAR(160), notes TEXT,
 result_schema_version INT NOT NULL, created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 FOREIGN KEY(revision_id) REFERENCES network_revisions(id), INDEX idx_optimization_date(created_at)
);
CREATE TABLE simulation_records (
 id BINARY(16) PRIMARY KEY, revision_id BINARY(16) NOT NULL, disabled_edge_ids_json JSON NOT NULL, result_json JSON NOT NULL,
 name VARCHAR(160), notes TEXT, result_schema_version INT NOT NULL, created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 FOREIGN KEY(revision_id) REFERENCES network_revisions(id), INDEX idx_simulation_date(created_at)
);
CREATE TABLE active_simulations (
 id BINARY(16) PRIMARY KEY, revision_number BIGINT NOT NULL, disabled_edge_ids_json JSON NOT NULL,
 version BIGINT NOT NULL DEFAULT 0, name VARCHAR(160), created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);
