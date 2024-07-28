import request from '@/utils/request'

export function getAllLink(query) {
    return request({
        url: '/link/getAllLink',
        method: 'get',
        headers: {
          isToken: false
        },
        params: query
    })
}

