<script>
import axios from 'axios';

export default {
    data() {
        return {
            fotos: {
                titolo: "",
                descrizione: "",
                url: "",
                visible: false,
                categorie: [],
            },
            titoloFoto: "",
            email: "",
            messaggio: "",
        };
    },
    methods: {
        getFoto() {
            axios
                .get("http://localhost:8080/api/foto/photos")
                .then((response) => {
                    this.fotos = response.data;
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        filterFoto() {
            axios
                .get(`http://localhost:8080/api/foto/filter?titolo=${this.titoloFoto}`)
                .then((response) => {
                    this.fotos = response.data;
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        createContatto() {
            const payload = {
                email: this.email,
                messaggio: this.messaggio
            };
            axios.post('http://localhost:8080/api/contatti/create', payload)
                .then(response => {
                    console.log('Messaggio inviato con successo');
                    this.email = "";
                    this.messaggio = "";
                    this.getFoto();
                })
                .catch(error => {
                    console.error(error);
                });
        }
    },
    mounted() {
        this.getFoto();
    },
}
</script>


<template>
    <form @submit.prevent="filterFoto()">
        <label for="titolo">Titolo Foto</label>
        <input type="text" id="foto" name="foto" v-model="titoloFoto">
        <button>Filtra</button>
    </form>
    <div>
        <ul v-for="foto in fotos" :key="foto.id">
            <li>
                <p>{{ foto.titolo }}</p>
                <p>{{ foto.descrizione }}</p>
                <p>{{ foto.url }}</p>
                <p>{{ foto.visibile }}</p>
                <ul>
                    <li v-for="categoria in foto.categorie" :key="categoria.id">
                        {{ categoria.nome }}
                    </li>
                </ul>
            </li>
        </ul>
    </div>
    <h4>Invia un messaggio</h4>
    <form>
        <label for="email">Email</label>
        <input type="text" id="email" name="email" v-model="email">

        <label for="messaggio">Messaggio</label>
        <input type="text" id="messaggio" name="messaggio" v-model="messaggio">

        <button @click.prevent="createContatto()">Invia</button>
    </form>
</template>

<style ></style>
