package com.algolia.search.serialize

import com.algolia.search.helper.toObjectID
import com.algolia.search.model.ObjectID
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.ArrayListClassDesc
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray


public object KSerializerObjectIDs : KSerializer<List<ObjectID>> {

    override val descriptor = ArrayListClassDesc(ObjectID.descriptor)

    override fun serialize(encoder: Encoder, obj: List<ObjectID>) {
        val json = jsonArray {
            obj.forEach {
                +json { KeyObjectID to it.raw }
            }
        }
        encoder.asJsonOutput().encodeJson(json)
    }

    override fun deserialize(decoder: Decoder): List<ObjectID> {
        return decoder.asJsonInput().jsonArray.map { it.jsonObject.getPrimitive(KeyObjectID).content.toObjectID() }
    }
}