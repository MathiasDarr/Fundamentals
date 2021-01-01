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
     {"name": "rideid", "type": "string"},
     {"name": "latitude", "type": "double"},
     {"name": "longitude", "type": "double"}
 ]
}
'''

avroDeserializer = AvroDeserializer(schema, schemaRegistryClient)
message = bytearray(b'\x00\x00\x00\x00\x01\nride2333333(@\x00\x00\x00\x00\x00\x00(@')
serializationContext = SerializationContext("coordinates", schema)

deserialized_message = avroDeserializer(message, serializationContext)