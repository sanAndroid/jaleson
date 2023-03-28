package com.github.sanandroid.jaleson.settings

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.Nullable

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
    name = "com.github.sanandroid.jaleson.settings.JlsForceState",
    storages = [Storage("SdkSettingsPlugin.xml")],
)
class JlsForceState : PersistentStateComponent<JlsForceState?> {

    @Nullable
    override fun getState() = this

    override fun loadState(state: JlsForceState) =
        XmlSerializerUtil.copyBean(state, this)

    var userId = ""
    var clientId = ""

    var baseUrl = ""
    var classPath: String? = null
    var packageName: String? = null

    val classesToImport = mutableListOf<String>()
    var filterLayoutable = true
    var filterCreatable = true
    var filterInterfaces = false

    var password: String?
        get() {
            val credentialAttributes = createCredentialAttributes(userId)
            val safe = PasswordSafe.instance
            val credential = safe.get(credentialAttributes)
            return credential?.password?.toString()
        }
        set(newPassword) {
            val credentialAttributes = createCredentialAttributes(userId)
            val safe = PasswordSafe.instance
            val credential = Credentials(userId, newPassword)
            safe.set(credentialAttributes, credential)
        }

    var clientSecret: String?
        get() {
            val credentialAttributes = createCredentialAttributes(clientId)
            val safe = PasswordSafe.instance
            val credential = safe.get(credentialAttributes)
            return credential?.password?.toString()
        }
        set(newClientSecret) {
            val credentialAttributes = createCredentialAttributes(clientId)
            val safe = PasswordSafe.instance
            val credential = Credentials(clientId, newClientSecret)
            safe.set(credentialAttributes, credential)
        }

    private fun createCredentialAttributes(key: String): CredentialAttributes {
        val serviceName = generateServiceName("com.github.sanandroid.jaleson.settings.JForceState", key)
        return CredentialAttributes(serviceName, key)
    }

    companion object {
        val instance: JlsForceState
            get() = ApplicationManager.getApplication().getService(JlsForceState::class.java)
    }
}
