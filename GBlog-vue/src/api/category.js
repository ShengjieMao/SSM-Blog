import request from '@/utils/request'

export function getCategoryList() {
    return request({
        url: '/category/getCategoryList',
        headers: {
          isToken: false
        },
        method: 'get'
    })
}
