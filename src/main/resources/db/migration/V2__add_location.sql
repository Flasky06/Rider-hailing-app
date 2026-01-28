ALTER TABLE riders ADD COLUMN current_lat DOUBLE;
ALTER TABLE riders ADD COLUMN current_lon DOUBLE;

ALTER TABLE delivery_requests ADD COLUMN pickup_lat DOUBLE;
ALTER TABLE delivery_requests ADD COLUMN pickup_lon DOUBLE;
ALTER TABLE delivery_requests ADD COLUMN dropoff_lat DOUBLE;
ALTER TABLE delivery_requests ADD COLUMN dropoff_lon DOUBLE;
