package com.example.nhlmobileapp.helpers

import java.io.IOException
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

class RSAEncryption {

    companion object {
        fun encrypt(plainText: String): String {
            val cipher: Cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey())
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.toByteArray()))
        }

        @Throws(GeneralSecurityException::class, IOException::class)
        private fun loadPublicKey(): Key {
            val key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxiuOSUp3HuyaScR768yicQOqYnXYovYUbTNSaTJsA4RkZ3wqgvlpCqBKEmAJwozK4Maebr/Z1WZc+ivt5LSnEmlnT32+UHEMq6z3RjsflFNQ4X0AzfNmJFb/BuZQvaDOADuKzzC4Lu2GDUzucQyE/zDcaQq7BSK9+g/F3F0KPzzO6k/ktfBQlxzWaxcyg6PrUn3N6IXR4IbPiTl7Ev+DGfgGO1ROijO3jJib48AJ1IAxn+zTYzJqSnGhFzexWxmoOVWBzXrNGcYfGWvlM5zV76i0ZRI+ftg8UU8Ykkm5tZ0vTa1k79RtHowtBhoMssvOva5c3EEdTJhDuSCeHr/GwwIDAQAB"
            val data: ByteArray = Base64.getDecoder().
            decode(key.toByteArray())
            val spec = X509EncodedKeySpec(data)
            val fact = KeyFactory.getInstance("RSA")
            return fact.generatePublic(spec)
        }
    }

}