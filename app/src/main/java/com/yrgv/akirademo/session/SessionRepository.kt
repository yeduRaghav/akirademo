package com.yrgv.akirademo.session

/**
 * Authentication & session related objects need to be stored.
 * Repository pattern not implemented here, but is ideally be backed by a local-storage and network sources.
 * Values setup upon login and cleared upon logout.
 */
object SessionRepository {

    fun getPlacesApiKey(): String {
        return "AIzaSyAgR46z0WAnh-G930Hb8DJ4M3DIY5nq8qI"
    }

}
