package com.example.smarthouse.Tools

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {
    private var supabaseClient: SupabaseClient? = null
    fun getSupabaseClient(): SupabaseClient {
        if (supabaseClient == null) {
            val supabaseUrl = "https://dqjyaffhgdrfsiabzmyi.supabase.co"
            val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRxanlhZmZoZ2RyZnNpYWJ6bXlpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA1ODA3MTEsImV4cCI6MjAxNjE1NjcxMX0.HlrbHC8AFPB8UxXM6aH3IZ_qdtw_xwE6Tq1ToN6oj0M"

            supabaseClient = createSupabaseClient(supabaseUrl, supabaseKey) {
                install(GoTrue)
                install(Postgrest)
            }
        }
        return supabaseClient!!
    }
}