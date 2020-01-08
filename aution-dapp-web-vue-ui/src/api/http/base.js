
import request from './require'
import forEach from 'lodash/forEach'

export default class BaseApi {
    constructor ( url ){
        this.url = url;
        this.fetch = request
    }
}