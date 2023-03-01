package com.appsnado.haippNew.adblock.util.hash

import com.appsnado.haippNew.database.adblock.Host
import java.io.Serializable

/**
 * A [HashingAlgorithm] of type [Host] backed by the [MurmurHash].
 */
class MurmurHashHostAdapter : HashingAlgorithm<Host>, Serializable {

    override fun hash(item: Host): Int = MurmurHash.hash32(item.name)

}
