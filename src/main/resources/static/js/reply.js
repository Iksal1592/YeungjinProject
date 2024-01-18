


async function addReply(replyObj){
    const response = await axios.post(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/replyregister`, replyObj)
    return response.data
} //댓글 작성

async function getReply(rno){
    const response = await axios.get(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/read/${rno}`)
    return response.data
} //댓글 읽기

async function modifyReply(replyObj){
    const response = await axios.put(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/${replyObj.rno}`, replyObj)
    return response.data
} //댓글 수정

async function removeReply(rno){
    const response = await axios.delete(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/${rno}`)
    return response.data
} //댓글 삭제

async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/${bno}`, {params:{page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return getList({bno:bno, page:lastPage, size:size})
    }

    return result.data
} //댓글 목록화 및 페이지네이션



/* ===============================================▼ TradeBoard reply ================================================================*/

async function tradeaddReply(replyObj){
    const response = await axios.post(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/tradereplyregister`, replyObj)
    return response.data
} //댓글 작성


async function tradegetReply(rno){
    const response = await axios.get(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/tradereplyread/${rno}`)
    return response.data
} //댓글 읽기


async function trademodifyReply(replyObj){
    const response = await axios.put(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/tradereplymodify/${replyObj.rno}`, replyObj)
    return response.data
} //댓글 수정


async function traderemoveReply(rno){
    const response = await axios.delete(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/tradereplydelete/${rno}`)
    return response.data
} //댓글 삭제


async function tradegetList({bno, page, size, goLast}){
    const result = await axios.get(`/TopMenu/MyLocal/LocalBoard/localboardread/replies/tradereplylist/${bno}`, {params:{page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return tradegetList({bno:bno, page:lastPage, size:size})
    }

    return result.data
} //댓글 목록화 및 페이지네이션






