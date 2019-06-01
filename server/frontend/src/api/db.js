import request from '@/utils/request'

async function select(tableName,params) {
  return await request({
    url: `/db/${tableName}`,
    method: 'get',
    params
  })
}
async function selectOne(tableName,id) {
  const params = {q:JSON.stringify({$where:id})}
  const results =  await request({
    url: `/db/${tableName}`,
    method: 'get',
    params
  })
  return _.get(results,"data[0]")
}
async function update(tableName,data) {
  return await request({
    url: `/db/${tableName}`,
    method: 'post',
    data
  })
}

async function remove(tableName,params) {
  return await request({
    url: `/db/${tableName}`,
    method: 'delete',
    params,
  })
}

async function insert(tableName,data) {
  return await request({
    url: `/db/${tableName}`,
    method: 'put',
    data,
  })
}

export default {
  select,
  selectOne,
  update,
  remove,
  insert
}
