/* ============================================================
   ODIN
   SCRIPT GENERAL
   Gestión de trámites + paginación + observaciones
   ============================================================ */

(function () {

    'use strict';


    /* ========================================================
       CSRF
       ======================================================== */

    function csrfHeaders() {

        const token =
            document.querySelector(
                'meta[name="_csrf"]'
            )?.content;

        const header =
            document.querySelector(
                'meta[name="_csrf_header"]'
            )?.content;


        const headers = {
            'Content-Type': 'application/json'
        };


        if (token && header) {

            headers[header] = token;

        }


        return headers;
    }


    /* ========================================================
       PAGINACIÓN DE GESTIÓN DE TRÁMITES
       ======================================================== */

    function iniciarPaginacionTramites() {

        const table =
            document.getElementById(
                'tablaTramites'
            );

        const pagination =
            document.getElementById(
                'tramitesPagination'
            );


        /*
         * Si estamos en otra pantalla del sistema,
         * simplemente no hacemos nada.
         */
        if (!table || !pagination) {

            return;

        }


        const tbody =
            document.getElementById(
                'tramitesTableBody'
            );


        if (!tbody) {

            return;

        }


        /*
         * Únicamente filas reales de radicados.
         *
         * Esto evita contar la fila:
         * "No hay radicados registrados".
         */
        const rows =
            Array.from(
                tbody.querySelectorAll(
                    '.tramite-row'
                )
            );


        const previousButton =
            document.getElementById(
                'paginationPrev'
            );

        const nextButton =
            document.getElementById(
                'paginationNext'
            );

        const pagesContainer =
            document.getElementById(
                'paginationPages'
            );

        const fromElement =
            document.getElementById(
                'paginationFrom'
            );

        const toElement =
            document.getElementById(
                'paginationTo'
            );

        const totalElement =
            document.getElementById(
                'paginationTotal'
            );


        /*
         * =====================================================
         * CONFIGURACIÓN
         * =====================================================
         *
         * Cambia este valor si después quieres:
         *
         * 5 registros  -> 5
         * 10 registros -> 10
         * 20 registros -> 20
         */

        const PAGE_SIZE = 10;


        let currentPage = 1;


        const totalRecords =
            rows.length;


        const totalPages =
            Math.max(
                1,
                Math.ceil(
                    totalRecords / PAGE_SIZE
                )
            );


        /*
         * TOTAL
         */

        if (totalElement) {

            totalElement.textContent =
                totalRecords;

        }


        /*
         * =====================================================
         * SIN REGISTROS
         * =====================================================
         */

        if (totalRecords === 0) {

            pagination.style.display =
                'none';

            return;

        }


        /*
         * =====================================================
         * RENDERIZAR PÁGINA
         * =====================================================
         */

        function renderPage() {

            /*
             * Protección por si la página actual
             * queda fuera del rango.
             */

            if (currentPage < 1) {

                currentPage = 1;

            }


            if (currentPage > totalPages) {

                currentPage =
                    totalPages;

            }


            const startIndex =
                (currentPage - 1)
                * PAGE_SIZE;


            const endIndex =
                startIndex
                + PAGE_SIZE;


            /*
             * Mostrar solamente las filas
             * pertenecientes a esta página.
             */

            rows.forEach(
                (row, index) => {

                    if (
                        index >= startIndex &&
                        index < endIndex
                    ) {

                        row.style.display =
                            '';

                    } else {

                        row.style.display =
                            'none';

                    }

                }
            );


            /*
             * MOSTRANDO X - Y DE Z
             */

            if (fromElement) {

                fromElement.textContent =
                    startIndex + 1;

            }


            if (toElement) {

                toElement.textContent =
                    Math.min(
                        endIndex,
                        totalRecords
                    );

            }


            /*
             * BOTÓN ANTERIOR
             */

            if (previousButton) {

                previousButton.disabled =
                    currentPage === 1;

            }


            /*
             * BOTÓN SIGUIENTE
             */

            if (nextButton) {

                nextButton.disabled =
                    currentPage ===
                    totalPages;

            }


            /*
             * NÚMEROS
             */

            renderPageNumbers();

        }


        /*
         * =====================================================
         * BOTONES NUMÉRICOS
         * =====================================================
         */

        function renderPageNumbers() {

            if (!pagesContainer) {

                return;

            }


            pagesContainer.innerHTML =
                '';


            /*
             * Si hay pocas páginas:
             *
             * 1 2 3 4 5
             *
             * Si hay muchas:
             *
             * 1 ... 4 5 6 ... 20
             */

            const pages =
                obtenerPaginasVisibles(
                    currentPage,
                    totalPages
                );


            pages.forEach(
                page => {


                    /*
                     * PUNTOS SUSPENSIVOS
                     */

                    if (page === '...') {

                        const dots =
                            document.createElement(
                                'span'
                            );


                        dots.className =
                            'pagination-dots';


                        dots.textContent =
                            '...';


                        pagesContainer.appendChild(
                            dots
                        );


                        return;

                    }


                    /*
                     * BOTÓN
                     */

                    const button =
                        document.createElement(
                            'button'
                        );


                    button.type =
                        'button';


                    button.textContent =
                        page;


                    button.className =
                        'pagination-page';


                    if (
                        page === currentPage
                    ) {

                        button.classList.add(
                            'active'
                        );

                        button.setAttribute(
                            'aria-current',
                            'page'
                        );

                    }


                    button.addEventListener(
                        'click',
                        () => {

                            currentPage =
                                page;


                            renderPage();


                            desplazarATabla();

                        }
                    );


                    pagesContainer.appendChild(
                        button
                    );


                }
            );

        }


        /*
         * =====================================================
         * DETERMINAR PÁGINAS VISIBLES
         * =====================================================
         */

        function obtenerPaginasVisibles(
            actual,
            total
        ) {

            /*
             * Hasta 7 páginas:
             *
             * mostramos todas.
             */

            if (total <= 7) {

                return Array.from(
                    {
                        length: total
                    },
                    (_, index) =>
                        index + 1
                );

            }


            /*
             * Inicio:
             *
             * 1 2 3 4 5 ... 20
             */

            if (actual <= 4) {

                return [
                    1,
                    2,
                    3,
                    4,
                    5,
                    '...',
                    total
                ];

            }


            /*
             * Final:
             *
             * 1 ... 16 17 18 19 20
             */

            if (
                actual >=
                total - 3
            ) {

                return [
                    1,
                    '...',
                    total - 4,
                    total - 3,
                    total - 2,
                    total - 1,
                    total
                ];

            }


            /*
             * Mitad:
             *
             * 1 ... 8 9 10 ... 20
             */

            return [
                1,
                '...',
                actual - 1,
                actual,
                actual + 1,
                '...',
                total
            ];

        }


        /*
         * =====================================================
         * SCROLL SUAVE A LA TABLA
         * =====================================================
         */

        function desplazarATabla() {

            /*
             * No hacemos scroll agresivo si el usuario
             * ya está viendo la tabla.
             */

            const rect =
                table.getBoundingClientRect();


            if (
                rect.top < 0 ||
                rect.top >
                window.innerHeight
            ) {

                table.scrollIntoView({
                    behavior:
                        'smooth',

                    block:
                        'start'
                });

            }

        }


        /*
         * =====================================================
         * ANTERIOR
         * =====================================================
         */

        if (previousButton) {

            previousButton.addEventListener(
                'click',
                () => {

                    if (
                        currentPage > 1
                    ) {

                        currentPage--;


                        renderPage();


                        desplazarATabla();

                    }

                }
            );

        }


        /*
         * =====================================================
         * SIGUIENTE
         * =====================================================
         */

        if (nextButton) {

            nextButton.addEventListener(
                'click',
                () => {

                    if (
                        currentPage <
                        totalPages
                    ) {

                        currentPage++;


                        renderPage();


                        desplazarATabla();

                    }

                }
            );

        }


        /*
         * =====================================================
         * INICIAR
         * =====================================================
         */

        renderPage();

    }


    /* ========================================================
       EXPORTAR TABLA DE TRÁMITES
       ======================================================== */

    function exportarTablaTramites() {

        const table =
            document.getElementById(
                'tablaTramites'
            );


        if (!table) {

            return;

        }


        /*
         * Encabezados.
         *
         * Quitamos la columna Acción.
         */

        const headers =
            Array.from(
                table.querySelectorAll(
                    'thead th'
                )
            )
                .slice(
                    0,
                    -1
                )
                .map(
                    cell =>
                        limpiarTextoCSV(
                            cell.innerText
                        )
                );


        /*
         * IMPORTANTE:
         *
         * Exportamos TODAS las filas,
         * incluso las ocultas por paginación.
         */

        const rows =
            Array.from(
                table.querySelectorAll(
                    'tbody .tramite-row'
                )
            )
                .map(
                    row =>

                        Array.from(
                            row.querySelectorAll(
                                'td'
                            )
                        )
                            .slice(
                                0,
                                -1
                            )
                            .map(
                                cell =>
                                    limpiarTextoCSV(
                                        cell.innerText
                                    )
                            )

                );


        const csvRows = [
            headers,
            ...rows
        ];


        const csv =
            csvRows
                .map(
                    row =>
                        row.join(';')
                )
                .join('\n');


        /*
         * BOM UTF-8.
         *
         * Ayuda a Excel con tildes,
         * ñ y caracteres especiales.
         */

        const blob =
            new Blob(
                [
                    '\uFEFF',
                    csv
                ],
                {
                    type:
                        'text/csv;charset=utf-8;'
                }
            );


        const url =
            URL.createObjectURL(
                blob
            );


        const link =
            document.createElement(
                'a'
            );


        link.href =
            url;


        link.download =
            `odin-tramites-${
                new Date()
                    .toISOString()
                    .slice(
                        0,
                        10
                    )
            }.csv`;


        document.body.appendChild(
            link
        );


        link.click();


        link.remove();


        URL.revokeObjectURL(
            url
        );

    }


    /* ========================================================
       LIMPIAR TEXTO PARA CSV
       ======================================================== */

    function limpiarTextoCSV(
        value
    ) {

        const text =
            String(
                value ?? ''
            )
                .replace(
                    /\s+/g,
                    ' '
                )
                .trim()
                .replace(
                    /"/g,
                    '""'
                );


        return `"${text}"`;

    }


    /* ========================================================
       GUARDAR OBSERVACIÓN
       ======================================================== */

    function iniciarFormularioObservacion() {

        const form =
            document.getElementById(
                'formObservacion'
            );


        if (!form) {

            return;

        }


        form.addEventListener(
            'submit',
            async event => {


                event.preventDefault();


                const input =
                    form.querySelector(
                        '[name="comentario"]'
                    );


                const comentario =
                    input
                        ?.value
                        ?.trim();


                if (!comentario) {

                    return;

                }


                const button =
                    form.querySelector(
                        'button[type="submit"]'
                    );


                if (button) {

                    button.disabled =
                        true;

                }


                try {


                    const response =
                        await fetch(
                            form.action,
                            {

                                method:
                                    'POST',

                                headers:
                                    csrfHeaders(),

                                body:
                                    JSON.stringify({
                                        comentario:
                                            comentario
                                    })

                            }
                        );


                    if (!response.ok) {

                        let message =
                            'No se pudo guardar la observación.';


                        try {

                            const data =
                                await response.json();


                            if (data.error) {

                                message =
                                    data.error;

                            }


                        } catch (_) {

                            /*
                             * La respuesta no era JSON.
                             */

                        }


                        throw new Error(
                            message
                        );

                    }


                    /*
                     * Recargamos para mostrar
                     * la observación recién guardada.
                     */

                    window.location.reload();


                } catch (error) {


                    alert(
                        error.message
                    );


                    if (button) {

                        button.disabled =
                            false;

                    }


                }


            }
        );

    }


    /* ========================================================
       EVENTOS DE GESTIÓN DE TRÁMITES
       ======================================================== */

    function iniciarGestionTramites() {

        /*
         * EXPORTAR
         */

        const exportButton =
            document.getElementById(
                'btnExportar'
            );


        if (exportButton) {

            exportButton.addEventListener(
                'click',
                exportarTablaTramites
            );

        }


        /*
         * PAGINACIÓN
         */

        iniciarPaginacionTramites();

    }


    /* ========================================================
       INICIO GENERAL
       ======================================================== */

    document.addEventListener(
        'DOMContentLoaded',
        () => {

            iniciarGestionTramites();

            iniciarFormularioObservacion();

        }
    );


})();