import {createSlice} from "@reduxjs/toolkit";

export const jwtSlice = createSlice({
    name: 'jwt',
    initialState: {
        value: '',
    },
    reducers: {
        setJwt: (state,jwt) => { state.value = jwt },
        eraseJwt: (state) => {state.value = ''}
    }
})

export const { setJwt, eraseJwt } = jwtSlice.actions

export default jwtSlice.reducer