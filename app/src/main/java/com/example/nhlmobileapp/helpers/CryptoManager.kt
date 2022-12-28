package com.example.nhlmobileapp.helpers

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager(
    val keyAlias: String
) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun getDecryptCypherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey: SecretKeyEntry? = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        return  existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(plainText: String, dir: File): String {
        val bytes = plainText.encodeToByteArray()
        val file = File(dir, "$keyAlias.txt")

        if (!file.exists()) {
            file.createNewFile()
        }

        val fos = FileOutputStream(file)

        return encrypt(bytes, fos).decodeToString()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray {
        val encryptedBytes = encryptCipher.doFinal(bytes)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    fun decrypt(dir: File): String {
        val file = File(dir, "$keyAlias.txt")

        if (!file.exists()) {
            return ""
        }

        return try {
            decrypt(FileInputStream(file))!!.decodeToString()
        } catch (e: Exception) {
            ""
        }

        
    }

    fun decrypt(inputStream: InputStream): ByteArray? {
        return try {
            inputStream.use {
                val ivSize = inputStream.read()
                val iv = ByteArray(ivSize)
                it.read(iv)

                val encryptedBytesSize = inputStream.read()
                val encryptedBytes = ByteArray(encryptedBytesSize)
                it.read(encryptedBytes)

                getDecryptCypherForIv(iv).doFinal(encryptedBytes)
            }
        } catch(e: Exception) {
            null
        }

    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    }
}