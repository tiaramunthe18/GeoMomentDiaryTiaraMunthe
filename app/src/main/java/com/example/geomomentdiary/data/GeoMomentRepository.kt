package com.example.geomomentdiary.data

class GeoMomentRepository(private val dao: GeoMomentDao) {

    val allMoments = dao.getAllMoments()

    fun search(keyword: String) = dao.searchMoment(keyword)

    suspend fun insert(moment: GeoMoment) {
        dao.insert(moment)
    }

    suspend fun update(moment: GeoMoment) {
        dao.update(moment)
    }

    suspend fun delete(moment: GeoMoment) {
        dao.delete(moment)
    }
}