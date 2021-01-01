from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext


schemaRegistryClient = SchemaRegistryClient({"url":"http://localhost:8081"})

schema = '''
{
"namespace": "org.mddarr.rides.event.dto",
 "type": "record",
 "name": "AvroRideCoordinate",
 "fields": [
         {"name": "dataID", "type": "string"},
         {"name": "value", "type": "double"}
 ]
}
'''

avroDeserializer = AvroDeserializer(schema, schemaRegistryClient)
message = bytearray(b'\x00\x00\x00\x00\x01Haa726535-38a7-4285-b70b-f4e8a8d97976\rH\xb2D|\x1f.@')
serializationContext = SerializationContext("time-series", schema)

deserialized_message = avroDeserializer(message, serializationContext)