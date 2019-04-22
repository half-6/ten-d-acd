import request from '@/utils/request'

async function getCancerType() {
    return await request({
        url: '/api/db/public.v_cancer_type',
        method: 'get',
        useCache: true
    })
}
async function getMachineType() {
    return await request({
        url: '/api/db/public.v_machine_type',
        method: 'get',
        useCache: true
    })
}
async function getAggTable() {
    return await request({
            url: '/api/db/v_agg_cancer_type',
            method: 'get',
        })
}
async function getROIImages(params) {
    return await request({
        url: '/api/db/v_roi_image',
        method: 'get',
        params
    })
}
async function saveImage(data) {
    return await request({
        url: '/api/image/save',
        method: 'post',
        data,
    })
}
async function updateROIImage(data) {
    return await request({
        url: '/api/db/roi_image',
        method: 'post',
        data,
    })
}
async function updateRecord(data) {
    return await request({
        url: '/api/db/record',
        method: 'post',
        data,
    })
}
async function getHospital(params) {
    return await request({
        url: '/api/db/public.v_hospital',
        method: 'get',
        params,
    })
}
async function detectImage(data) {
    return await request({
        url: '/api/image/recognition',
        method: 'post',
        data
})
}
export default {
    getCancerType,
    getHospital,
    getMachineType,
    getAggTable,
    saveImage,
    updateROIImage,
    updateRecord,
    detectImage,
    getROIImages
}
